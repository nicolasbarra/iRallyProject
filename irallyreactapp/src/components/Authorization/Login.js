import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import axios from 'axios';

class Login extends Component {
    state = {
        username: null,
        password: null
    };


    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
    };

  handleSubmit = (e) => { 
      e.preventDefault();
      if (!this.state.username || !this.state.password) {
        alert("please fill fields!")
      } else {
        axios.post('http://localhost:9000/admins/login', 
        {
            username: this.state.username, 
            password: this.state.password           
        })
        .then(res => {
            if (res.data.err){
                alert("please fill fields!")
            } else if (res.data.success) {
                this.props.changeState(true, res.data.username);
            } else {
                alert("username or password is incorrect");
                this.props.changeState(false, null);
            }
        });
    }
  };

  render() {  
     if (this.props.isAuthenticated) {
        return <Redirect to='/' />
     }   
      return(
        <div>
          <div className="container">
              <form className="form-horizontal" onSubmit={this.handleSubmit}>
                  <h2>Login</h2>
                  <div className="form-group row">
                      <label htmlFor="username" className="col-sm-3 control-label">Username</label>
                      <div className="col-sm-9">
                          <input type="text" id="username" placeholder="abc" className="form-control" name= "username" onChange={this.handleChange}/>
                      </div>
                  </div>
                  <div className="form-group row">
                      <label htmlFor="password" className="col-sm-3 control-label">Password</label>
                      <div className="col-sm-9">
                          <input type="password" id="password" placeholder="password" className="form-control" name = "password" onChange={this.handleChange}/>
                      </div>
                  </div>
                  <button type="submit" className="btn btn-primary btn-block">Login</button>
              </form> 
          </div>
        </div>
      )     
   }   
}   

export default Login;