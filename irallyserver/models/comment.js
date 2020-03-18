const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const commentSchema = new Schema(
    {
        commentId: {
            type: String,
            required: [true, 'comments need an id (should be unique)'],
            unique: true,
            trim: true
        },
        author: {
            type: mongoose.Schema.Types.Mixed,
            // used mixed type as the author can be an admin or a user
            required: [true, 'comments must have an author']
        },
        event: {
            type: mongoose.Schema.Types.ObjectID,
            ref: 'event',
            required: [true, 'comments must be on the wall of some event']
        },
        content: {
            type: String,
            required: [true, 'comments have to have content to the comment'],
        },
        replies: [
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

module.exports = mongoose.model('Comment', commentSchema);
