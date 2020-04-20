const express = require('express');
const router = express.Router();
const User = require('../models/user');
const Event = require('../models/event');
const Admin = require('../models/admin');


function getUsers(substrings) {
    User.find({"username" : {$in : substrings}}, (err, users) => {
        return users;
    }) 
}

function getEvents(substrings) {
    Event.find({"eventId" : {$in : substrings}}, (err, events) => {
        return events;
    }) 
}

function getAdmins(substrings) {
    Admin.find({username  : {$in : substrings}}, (err, admins) => {
        return admins;
    })
}


/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express'});
});

router.get('/search', function(req, res, next) {  
    var i, j, result = [];    
    for (i = 0; i < str.length; i++) {
        for (j = i + 1; j < str.length + 1; j++) {
            result.push(str.slice(i, j));
        }
    }
    Promise.all([
        getUsers(result),
        getEvents(result),
        getAdmins(result)
    ]).then(([users, events, admins]) => {
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
