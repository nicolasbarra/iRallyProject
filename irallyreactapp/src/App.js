import React, {Component} from 'react';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import Register from './components/Authorization/Register';
import Login from './components/Authorization/Login';
import Loading from './components/Authorization/Loading';
import Logout from './components/Authorization/Logout';
import Feed from './components/Feed'
import EventPage from './components/EventPage'
import NavBar from './components/NavBar';
import Profile from './components/Profile';
import StatPage from './components/StatPage'
import axios from 'axios';

class App extends Component {
  constructor() {
    super();
    this.state = {
      isAuthenticated: false,
      isAuthenticating: false,
      username: null,
    }
  }

  changeState = (val, name) => {
      this.setState({isAuthenticated: val, username: name});
  };

  componentDidMount() {
    this.setState({ isAuthenticating: true});
    axios.post("http://localhost:9000/admins/validateLogin") 
    .then (res => {
        if (res.data.user) {
            this.setState({ isAuthenticated: true});
            this.setState({username: res.data.user});
        } else {
            this.setState({ isAuthenticated: false });
        }
        this.setState({ isAuthenticating: false });
    })
  } 

  render() {
    if (this.state.isAuthenticating) {
      return <Loading />
    } else {
    return (
      <div className="App">
          <Router>
              <NavBar isAuth={this.state.isAuthenticated}
                      username={this.state.username}/>
              <Route exact path='/' render={(props) => <Profile {...props} {...this.state}/>}/>
              <Route exact path='/logout'
                     render={(props) => <Logout {...props} {...this.state} changeState={this.changeState}/>}/>
              <Route exact path='/login'
                     render={(props) => <Login {...props} {...this.state} changeState={this.changeState}/>}/>
              <Route exact path='/feed'
                     render={(props) => <Feed {...props} {...this.state} changeState={this.changeState}/>}/>       
              <Route exact path='/register'
                     render={(props) => <Register {...props} {...this.state} changeState={this.changeState}/>}/>
              <Route exact path='/statistics'
                     render={(props) => <StatPage {...props} {...this.state} changeState={this.changeState}/>}/>
              <Route exact path='/feed/:event'
              render={(props) => <EventPage {...props} {...this.state} changeState={this.changeState}/>}/>
          </Router>
      </div>
    );
    }
  }  
}

export default App;