const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const adminSchema = new Schema(
    {
        username: {
            type: String,
            required: true,
            unique: true,
            trim: true,
            minlength: 3,
            lowercase: true
        },
        password: {
            type: String,
            required: true,
            trim: true,
            minlength: 3
        },
        adminInfo: {
            adminName: {
                type: String,
                required: true,
                unique: true,
                trim: true,
                minlength: 3
            },
            description: {
                type: String,
                required: true,
                trim: true,
                minlength: 3
            },
            politicalAffiliation: {
                type: String,
                required: true,
                trim: true
            },
            goals: [
                {
                    type: String,
                    required: true,
                    trim: true
                }
            ],
            profilePictureLink: String,
            interests: [String],
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

const Admin = mongoose.model('Admin', adminSchema);

module.exports = Admin;
