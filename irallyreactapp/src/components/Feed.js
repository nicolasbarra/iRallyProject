import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import axios from 'axios';
import Loading from './Authorization/Loading';
import Event from './EventCard';
import "./Feed.css"

class Feed extends Component {
    state = {
        events: []
    }

    componentDidMount() {
        axios.post('http://localhost:9000/events/getAllEvents') 
        .then (res => {
            if (res.data.events) {
                console.log("WE GOT EVENTS BABY", res.data.events)
                this.setState({                  
                    loading: false,
                    events: res.data.events
                })     
            }  else {
                console.log("WE DONT HAVE EVENTS :(");
                this.setState({
                    loading: false,                   
                })  
            }    
        })     
      }

      render() {
        if (!this.props.isAuthenticated) {
            return <Redirect to='/login'/>
        } else {
            if (this.state.loading) {
                return <Loading/>
            } else {
                return (
                    <div className="container-fluid"> 
                        {this.state.events.map((event) => <Event currEvent={event}/>)}
                    </div>
                )   
            }     
        } 
    }    
    
}

export default Feed;