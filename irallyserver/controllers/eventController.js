const Event = require('../models/event');
const Admin = require('../models/admin');
const User = require('../models/user');
 
exports.create_event = [
    (req, res) => {
        if (req.body.isAdmin) {
            // TODO: fix the route for this case of the if else statment above
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

                event.save();  
            })       
               
        } else {
            User.findOne({'username': req.body.username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err
                    });
                } else if (user) {
                    const event = new Event (
                        {
                            eventId: req.body.eventName,
                            creator: user._id,
                            description: req.body.description,
                            address: req.body.address,
                            dateTime: req.body.dateTime,
                            numberOfAttendees: 0,
                            interestsOfAttendees: user.personalInfo.interests,
                        }
                    );
                    event.save((err, event) => {
                        if (err) {
                            return res.json({
                                status: 'Failure',
                                errors: err
                            });
                        } else {
                            if (user.numEventsCreated === 0) {
                                user.eventsCreated = [event._id];
                                user.numEventsCreated = user.numEventsCreated + 1;
                            } else {
                                user.numEventsCreated = user.numEventsCreated + 1;
                                user.eventsCreated.push(event._id);
                            }
                            user.save();
                        }
                    });
                } else {
                    // there is no user with that username
                    return res.json({
                        status: 'Failure',
                        errors: "Unable to create event: there is no user with that username"
                    });
                }
            });
        }        
    }
];

exports.add_attendees = [
    (req, res) => {
        Event.findOne({
            eventId: req.body.username + req.body.eventName
        }, (err, event) => {
            User.findOne({'username': req.body.username}, (err, user) => {       
                event.numberOfAttendees = event.numberOfAttendees + 1;
                event.attendees = event.attendees.push(user._id)
                event.save();
            });
        })
    }
];