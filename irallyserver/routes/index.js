const express = require('express');
const router = express.Router();
const User = require('../models/user');
const Event = require('../models/event');
const Admin = require('../models/admin');


function getUsers(substrings) {
    return new Promise(function(resolve, reject) {
        User.find({"username" : {$in : substrings}}, (err, users) => {
            //console.log("users", users);
            if (err) {
                reject(err);
            } else {
                let userList = []
                users.forEach(function(user) { 
                   userList.push(user.username);
                })
                resolve(userList);
            }
        }) 
    });   
}

function getEvents(substrings) {
    return new Promise(function(resolve, reject) {
        Event.find({"eventId" : {$in : substrings}}, (err, events) => {
            //console.log("events", events);
            if (err) {
                reject(err);
            } else {
                let eventList = []
                events.forEach(function(event) { 
                   console.log(event);
                   eventList.push(event.eventId + " on " + event.dateTime);
                })
                resolve(eventList);
            }
        }) 
    });
}

function getAdmins(substrings) {
    return new Promise(function(resolve, reject) {
        Admin.find({username  : {$in : substrings}}, (err, admins) => {
            //console.log("admins", admins);
            if (err) {
                reject(err);
            } else {
                let adminList = []
                admins.forEach(function(admin) {
                   adminList.push(admin.username);
                })
                resolve(adminList);
            }
        })
    });
}


/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express'});
});

router.post('/search', function(req, res, next) {  
    var i, j, result = [];    
    for (i = 0; i < req.body.query.length; i++) {
        for (j = i + 1; j < req.body.query.length + 1; j++) {
            result.push(req.body.query.slice(i, j));
        }
    }
    Promise.all([
        getUsers(result),
        getEvents(result),
        getAdmins(result)
    ]).then(([users, events, admins]) => {
        // console.log("users", users);
        // console.log("events", events);
        // console.log("admins", admins);
        return res.json({
            status: 'Success',
            errors: null,
            usersList: users,
            eventsList: events, 
            adminsList: admins
        });
    }).catch((err) => {
        if (err) {
            return res.json({
                status: 'Failure',
                errors: err
            });
        }
    });    
})

module.exports = router;
