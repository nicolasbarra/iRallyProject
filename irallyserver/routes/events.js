const express = require('express');
const router = express.Router();

const event_controller = require('../controllers/eventController');

router.post('/', event_controller.event);

router.post('/create', event_controller.create_event);

// router.post('/delete', event_controller.create_event);

router.post('/addAttendee', event_controller.add_attendee);


router.post('/getClosestEvents', event_controller.grab_closest_events);

module.exports = router;
