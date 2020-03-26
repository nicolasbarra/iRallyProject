const express = require('express');
const router = express.Router();

const admin_controller = require('../controllers/adminController');

/* GET admins listing. */
router.get('/', (req, res, next) => {
    res.send('respond with a resource');
});

router.post('/create', admin_controller.create_admin);

// router.post('/delete', admin_controller.delete_admin);

router.post('/login', admin_controller.login_admin);

router.post('/validateLogin', admin_controller.validate_login_admin);

router.post('/logout', admin_controller.logout_admin);

module.exports = router;
