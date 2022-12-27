/* jshint esversion: 6 */
const express = require('express'),
    http = require('http'),
    app = express(),
    server = http.createServer(app),
    io = require('socket.io').listen(server),
    lowdb = require('lowdb'),
    FileSync = require('lowdb/adapters/FileSync'),
    adapter = new FileSync('db.json'),
    db = lowdb(adapter);

if (!db.has('messages').value()) {
    db.set('messages', []).write();
}
if (!db.has('walls').value()) {
    db.set('walls', []).write();
    console.log('deleted all walls');
    db.get('walls')
        .push(
            {
                id: 0,
                title: 'GTU Bilmuh',
                latitude: 1,
                longtitude: 1
            }, {
                id: 1,
                title: 'GTU Yabancı Dil',
                latitude: 1,
                longtitude: 10
            }
        ).write();
   
    console.log('created new walls');
}



app.get('/walls', (req, res) => {
    const {
        latitute,
        longtitude
    } = req.query;
    res.send(db.get('walls').value());
    console.log('walls sent', db.get('walls').value());
});

app.get('/walls/:id', (req, res) => {
    res.send(db.get(`messages`).filter({wallid:parseInt(req.params.id)}));
})

app.post('/walls', (req, res) => {
    const {
        latitude,
        longtitude,
        title
    } = req.body;
    db.get('walls').push({
        latitude: latitude,
        longtitude: longtitude,
        title: title,
        id: db.get('walls').size().value()
    }).write();
    res.status(204).end();
});

io.on('connection', (socket) => {
    console.log('user connected');
    socket.on('join', function (userNickname) {
        console.log(userNickname + " : has joined the chat ");
        socket.broadcast.emit('userjoinedthechat', userNickname + " : has joined the chat ");
    });

    /**
     * {
     *  walls: [0],
     *  latitude: 10,
     *  longtitude: 1,
     *  nickname: gebze1,
     *  message: 'yarın bil 101 dersi yok'
     * }
     */
    socket.on('messagedetection', (nickname, message,latitude,longtitude) => {
        //log the message in console 
        console.log(nickname + " : " + message + latitude + longtitude);
        //create a message object 
        let msg = function (id) {
            return {
                latitude: latitude,
                longtitude: longtitude,
                message: message,
                nickname: nickname,
                wallid: id
            };
        };

        db.get('walls').value().filter((val) => {
		console.log(val);
           
            return Math.sqrt(Math.pow(val.latitude - latitude, 2) + Math.pow(val.longtitude - longtitude, 2)) < 500;
        }).forEach(function (wall) {
			console.log(wall);
            io.emit(wall.title, msg(wall.id));
           
			db.get(`messages`).push(msg(wall.id)).write();
        });

        // send the message to all users including the sender  using io.emit() 
    });

    socket.on('disconnect', function () {
        console.log('User has left ');
        socket.broadcast.emit("userdisconnect", ' user has left');
    });
});

server.listen(3000, () => {
    console.log('Node app is running on port 3000')
});
