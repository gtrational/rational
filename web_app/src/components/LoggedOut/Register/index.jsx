import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import TextField from 'material-ui/TextField';

import {register} from '../../../services/WebAPI';

class Register extends Component {

  constructor(props) {
    super(props);
    this.state={
      username:'',
      password:'',
      confirm_password:'',
      email:'',
      error:''
    }
  }

  handleSubmit(e) {
    // something to handle submitting...
    // possibly with database stuff, I think
    console.log("values",this.state.username,this.state.password,this.state.email);
    if (this.state.username == '') {
      this.setState({error:"Please enter username"})
    } else if (this.state.password == '') {
      this.setState({error:"Please enter password"})
    } else if (this.state.confirm_password == '') {
      this.setState({error:"Please confirm password"})
    } else if (this.state.email == '') {
      this.setState({error:"Please enter email"})
    } else {
      if (this.state.password != this.state.confirm_password) {
        this.setState({error:"Passwords do not match"})
      } else {
        this.setState({error:""})
      }
    }

    register(this.state.username, this.state.password)
        .then(function (response) {
            console.log(response)
            // Route Stuff
        }, function(err) {
            console.log(err)
            this.setState({error:"Registration Error"})
        })

  }

  render() {
    return (
      <div>
        <MuiThemeProvider>
        <AppBar
          title="Register"
        />
        <TextField
        hintText="Enter Email"
        floatingLabel="Email"
        onChange = {(event, newValue) => this.setState({email:newValue})}
        />
        <br/>

        <TextField
        hintText="Enter Username"
        floatingLabel="Username"
        onChange = {(event, newValue) => this.setState({username:newValue})}
        />
        <br/>

        <TextField
        hintText="Enter Password"
        floatingLabel="Password"
        onChange = {(event, newValue) => this.setState({password:newValue})}
        />
        <br/>

        <TextField
        hintText="Confirm Password"
        floatingLabel="Confirm Password"
        onChange = {(event, newValue) => this.setState({confirm_password:newValue})}
        />
        <br/>
        <br/>

        <RadioButtonGroup defaultSelected="user" style={{ display: 'flex', justifyContent: 'center'}}>
          <RadioButton name="User" value="user" label="User" style={{ width: 'auto' }} align="centers"/>
          <RadioButton name="Admin" value="admin" label="Admin" style={{ width: 'auto' }} align="center"/>

        </RadioButtonGroup>
        <br/>

        <RaisedButton label="Submit" primary={true} onClick={this.handleSubmit.bind(this)} />
        <br/>
        <br/>
        {this.state.error}
        </MuiThemeProvider>
      </div>
    );
  }
}

export default Register;
