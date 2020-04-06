const Event = require('../models/event');
const Admin = require('../models/Admin');
const User = require('../models/User');

exports.create_event = [
    (req, res) => {
        if (admin) {
            Admin.findOne({'username': req.body.username}, (err, admin) => {             
                const event = new Event ({
                    eventId: admin.username + req.body.eventName + admin.numEventsCreated,
                    eventName: req.body.eventName,
                    creator: admin._id,
                    description: "",
                    address:"",
                    dateTime: "",
                    attendees:"",
                    numberOfAttendees: "",
                    interestsOfAttendees: "",
                    comments:""
                });
            })            
        } else {
            User.findOne({'username': req.body.username}, (err, user) => {             
                const event = new Event ({
                    eventId: user.username + req.body.eventName + user.numEventsCreated,
                    eventName: user.body.eventName,
                    creator: user._id,
                    description: req.body.description,
                    address: req.body.address,
                    dateTime: req.body.dateTime,
                    attendees: req.body.attendees,
                    numberOfAttendees: req.body.numberOfAttendees,
                    interestsOfAttendees: req.body.interestsOfAttendees,
                    comments: ""
                });
            })      
        }        
    }
];
