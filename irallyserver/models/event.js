const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const eventSchema = new Schema(
    {
        eventId: {
            type: String,
            required: [true, 'event needs a name (should be unique)'],
            unique: true,
            trim: true,
            minlength: 3
        },
        creator: {
            type: mongoose.Schema.Types.Mixed,
            // used mixed type as the creator can be an admin or a user
            required: [true, 'events must have a creator']
        },
        creatorId: {
            type: String,
            required: [true, 'events must have a creator whose id is store here']
        },
        description: {
            type: String,
            required: [true, 'events need descriptions'],
            minlength: 3
        },
        address: {
            type: String,
            required: [true, 'events need addresses']
        },
        dateTime: {
            // TODO: make the type of this Date
            type: String,
            required: [true, 'events need a date and time']
        },
        attendeesRefs: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        attendeesStrings: [
            {
                type: String
            }
        ],
        numberOfAttendees: {
            type: Number,
            required: [true, 'events must have number of attendees, may be zero']
        },
        interestsOfAttendees: [
            {
                type: String,
                required: [true, 'need to have some interest, start with those of creator']
            }
        ],
        commentsRefs: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'comment'
            }
        ],
        commentsStrings: [
            {
                type: String
            }
        ]
    },
    {
        timestamps: true,
    }
);

module.exports = mongoose.model('Event', eventSchema);
