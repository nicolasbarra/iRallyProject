const express = require('express');
const router = express.Router();

const user_controller = require('../controllers/userController');

/* GET users listing. */
router.get('/', (req, res, next) => {
  // TODO: some kind of error because I don't think this page should happen
  res.send('respond with a resource');
});

router.post('/create', user_controller.create_user);

// router.post('/delete', user_controller.delete_user);
//
// router.post('/login', user_controller.login_user);
//
// router.post('/addInterest', user_controller.add_interest);
//
// router.post('/removeInterest', user_controller.remove_interest);

module.exports = router;
