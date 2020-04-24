import React, {Component} from 'react'
import {Link} from 'react-router-dom';
import "./EventCard.css"

class EventCard extends Component {
    render() {
        console.log("event: ", this.props.currEvent);
        return(
            <div class="card text-center">          
                <div class="card-body">
                    <h5 class="card-title">{this.props.currEvent.eventId}</h5>
                    <p class="card-text">{this.props.currEvent.description}</p>
                    <Link to={'/feed/' + this.props.currEvent.eventId}>
                        <button type="button" className="btn btn-primary">MoreInfo</button>
                    </Link>      
                    <a href="#" class="btn btn-primary">Delete</a>
                </div>
                <div class="card-footer text-muted">
                    {this.props.currEvent.dateTime}
                </div>
            </div>
        )
    }
}

export default EventCard;