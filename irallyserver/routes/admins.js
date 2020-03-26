const express = require('express');
const router = express.Router();

const admin_controller = require('../controllers/adminController');

/* GET admins listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

router.post('/addAdmin', admin_controller.add_admin);

router.post('/checkLoggedIn', admin_controller.check_login);

router.post('/logout', admin_controller.logout_admin);

router.post('/checkAdmin', admin_controller.check_admin);

module.exports = router;
