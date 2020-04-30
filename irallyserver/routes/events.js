const express = require('express');
const router = express.Router();

const event_controller = require('../controllers/eventController');

router.post('/', event_controller.event);

router.post('/create', event_controller.create_event);

// router.post('/delete', event_controller.create_event);

router.post('/addAttendee', event_controller.add_attendee);

router.post('/addComment', event_controller.add_comment);

router.post('/getClosestEvents', event_controller.grab_closest_events);

router.post('/getAllEvents', event_controller.grab_all_events);

router.post('/deleteEvent', event_controller.delete_event);


module.exports = router;
