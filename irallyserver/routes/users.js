const express = require('express');
const router = express.Router();

const user_controller = require('../controllers/userController');

/* GET users listing. */
router.get('/', user_controller.profile_user);

router.post('/create', user_controller.create_user);

router.post('/delete', user_controller.delete_user);

router.post('/login', user_controller.login_user);

router.post('/addInterest', user_controller.add_interest);

router.post('/removeInterest', user_controller.remove_interest);

router.post('/addFriend', user_controller.add_friend);

router.post('/removeFriend', user_controller.remove_friend);

module.exports = router;
