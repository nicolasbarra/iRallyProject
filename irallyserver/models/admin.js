const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const adminSchema = new Schema(
    {
        username: {
            type: String,
            required: [true, 'need a username'],
            unique: true,
            trim: true,
            minlength: 3,
            lowercase: true
        },
        password: {
            type: String,
            required: [true, 'need a password'],
            trim: true,
            minlength: 3
        },
        adminInfo: {
            adminName: {
                type: String,
                required: [true, 'admin account needs a name'],
                unique: true,
                trim: true,
                minlength: 3
            },
            description: {
                type: String,
                required: [true, 'need to have a description'],
                trim: true,
                minlength: 3
            },
            politicalAffiliation: {
                type: String,
                required: [true, 'need to give some political affiliation'],
                trim: true
            },
            goals: [
                {
                    type: String,
                    required: [true, 'need to give some goals'],
                    trim: true
                }
            ],
            interests: [
                {
                    type: String,
                    required: [true, 'need to have some interest']
                }
            ],
            profilePictureLink: String
        },
        currentEventAttendees: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        pastEventAttendees: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        eventsToHost: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'event'
            }
        ],
        eventsHosted: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'event'
            }
        ],
        comments: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'comment'
            }
        ]
    },
    {
        timestamps: true,
    }
);

module.exports = mongoose.model('Admin', adminSchema);
