const Event = require('../models/event');
const Admin = require('../models/admin');
const User = require('../models/user');

var getDistance = function(p1, p2) {
    var R = 6378137; // Earth’s mean radius in meter
    var dLat = rad(p2.lat - p1.lat);
    var dLong = rad(p2.lng - p1.lng);
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(rad(p1.lat)) * Math.cos(rad(p2.lat)) *
      Math.sin(dLong / 2) * Math.sin(dLong / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c;
    return d; // returns the distance in meter
};
 
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
                            creatorId: user.username,
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
                                errors: 'An event with that name already exists. Please try another.'
                            });
                        } else {
                            if (user.numEventsCreated === 0) {
                                user.eventsCreated = [event._id];
                                user.eventsCreatedStrings = [event.eventId + ' on ' + event.dateTime];
                                user.numEventsCreated = user.numEventsCreated + 1;
                            } else {
                                user.numEventsCreated = user.numEventsCreated + 1;
                                user.eventsCreatedRefs.push(event._id);
                                user.eventsCreatedStrings.push(event.eventId + ' on ' + event.dateTime);
                            }
                            user.save();
                            return res.json({
                                status: 'Success',
                                errors: null,
                                event: event
                            });
                        }
                    });
                } else {
                    // there is no user with that username
                    return res.json({
                        status: 'Failure',
                        errors: 'Unable to create event: there is no user with that username'
                    });
                }
            });
        }        
    }
];

exports.add_attendee = [
    (req, res) => {
        Event.findOne({eventId: req.body.eventId}, (err, event) => {
            if (err) {
                return res.json({
                    status: 'Failure',
                    errors: err
                });
            } else if (event) {
                User.findOne({'username': req.body.username}, (err, user) => {
                    if (err) {
                        return res.json({
                            status: 'Failure',
                            errors: err
                        });
                    } if (user) {
                        if (event.numberOfAttendees == 0) {
                            event.attendeesRefs = [user._id];
                            event.attendeesStrings = [user.username];
                        } else {
                            event.attendeesRefs.push(user._id);
                            event.attendeesStrings.push(user.username);
                        }
                        event.numberOfAttendees = event.numberOfAttendees + 1;
                        // TODO: add the interests of the attendees
                        event.save();
                        if (user.eventsToAttendRefs) {
                            user.eventsToAttendRefs.push(event._id);
                            user.eventsToAttendStrings.push(event.eventId + ' on ' + event.dateTime)
                        } else {
                            user.eventsToAttendRefs = [event._id];
                            user.eventsToAttendStrings = [event.eventId + ' on ' + event.dateTime];
                        }
                        console.log("PLEASE WORK GOD PLEASE");
                        user.save();
                        return res.json({
                            status: 'Success',
                            errors: null
                        });
                    } else {
                        return res.json({
                            status: 'Failure',
                            errors: 'Unable to join event: User cannot be found.'
                        });
                    }
                });
            } else {
                // there is no event with that eventId
                return res.json({
                    status: 'Failure',
                    errors: 'Unable to find event: there is no event with that name.'
                });
            }
        })
    }
];

exports.event = [
    (req, res) => {
        Event.findOne({'eventId': req.body.eventId}, (err, event) => {
            if (err) {
                return res.json({
                    status: 'Failure',
                    errors: err
                });
            } else if (event) {
                return res.json({
                    status: 'Success',
                    errors: null,
                    event: event
                });
            } else {
                // there is no event with that eventId
                return res.json({
                    status: 'Failure',
                    errors: "Unable to find event: there is no event with that name."
                });
            }
        });
    }
];


exports.grab_closest_events = [
    (req, res) => { 
        url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + userData[0].address + userData[0].city + userData[0].state + "&key=AIzaSyB2_4SMM3Bxc-XE90LPTqao04m_XYkbsEw"
        axios.get(url).then(() => {
            //you get the latitude and longitude 
            ///
            Event.findAll(){
                
            }

        })
    }
]