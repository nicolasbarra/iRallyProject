import React, {Component} from 'react'
import "./EventPage.css"
import axios from "axios"
import {Redirect} from 'react-router-dom';
import Loading from './Authorization/Loading'
class EventPage extends Component {

    state = {
        currEvent: null,
        loading: true
    }

    componentDidMount() {
        axios.post("http://localhost:9000/events/", {eventId : this.props.match.params.event})
        .then((response) => {
            if (response.data.event) {
                this.setState({
                    currEvent: response.data.event,
                    loading: false
                })
            }
        })
    }

    deleteEvent = () => {
        axios.post("http://localhost:9000/events/deleteEvent", {eventId : this.state.currEvent.eventId})
        .then((response) => {
            if (response.data.event) {
                return <Redirect to='/feed'/>
            }
        })
    }

    render() {
        console.log("THIS IS THE CURRENT EVENT", this.state.currEvent)
        if (!this.props.isAuthenticated) {
            return <Redirect to='/login'/>
        } else {
            if (this.state.loading) {
                return <Loading/>
            } else {
                return(
                    <div className = "text-align: center">
                        <div class="card text-center">          
                            Event Name: {this.state.currEvent.eventId}
                        </div>
                        <div class="card text-center">          
                            Event Description: {this.state.currEvent.description}
                        </div>
                        <div class="card text-center">          
                            Creator: {this.state.currEvent.creatorId}
                        </div>
                        <div class="card text-center">          
                            Attendees: {this.state.currEvent.attendeesStrings.map((attendee) => <div>{attendee}</div>)}
                        </div>
                        <div class="card text-center">          
                            DateTime: {this.state.currEvent.dateTime}
                        </div>
                        <div class="card text-center">          
                            Address: {this.state.currEvent.address}
                        </div>
                        <button className="btn btn-primary" onClick={() => this.deleteEvent()}>Delete</button>
                    </div>
                )
            }
        }
    }
}

export default EventPage;