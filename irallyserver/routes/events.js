const express = require('express');
const router = express.Router();

const event_controller = require('../controllers/eventController');

/* GET events listing. */
router.get('/', function (req, res, next) {
    res.send('respond with a resource');
});

router.post('/create', event_controller.create_event);

module.exports = router;
