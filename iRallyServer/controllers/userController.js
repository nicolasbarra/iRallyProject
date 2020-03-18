const { body, check, validationResult } = require("express-validator");

const User = require('../models/user');

// exports.create_user = [
// //     check('username').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
// //     check('password').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
// //     check('name').isString().notEmpty().trim().isLength({ min: 3 }.escape()),
// //     check('email').isString().notEmpty().trim().isLength({ min: 3 }).isEmail().normalizeEmail(),
// //     check('gender').notEmpty().escape(),
// //     check('genderPronouns').notEmpty().escape(),
// //     check('interests').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
// //     (req, res) => {
// //         const errors = validationResult(req);
// //         if (!errors.isEmpty()) {
// //             // TODO: this may be incorrect
// //             return res.status(422).json({ errors: errors.array() });
// //         } else {
// //             // TODO: route content here
// //         }
// //     }
// // ];