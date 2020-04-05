import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import axios from 'axios';
import Loading from "./Loading";


class Logout extends Component {
  state = {
      isAuthenticated: false,
      isAuthenticating: false,
  };

  componentDidMount() {
    axios.post('http://localhost:9000/admins/logout') 
    .then (res => {
        if (res.data.success) {
            this.props.changeState(false, null);
        }
    })     
  }
  
  render() {
    if (this.props.isAuthenticating) {
        return <Loading />
    } else {
        return (<Redirect to="/login"/>);
    }
  }     
}

export default Logout;