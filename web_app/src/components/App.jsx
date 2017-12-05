import React, { Component } from 'react'
import { BrowserRouter as Router, Route, IndexRoute } from 'react-router-dom'

import NotFound from '../components/global/NotFound'
import LoggedOut from '../components/LoggedOut'
import LoggedIn from '../components/LoggedIn'

function requireLoggedIn(nextState, replaceState) {
    // TODO: check logged in
}

import logo from '../logo.svg';
import './App.css';
import * as WebAPI from '../services/WebAPI.js';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload.
        </p>
      </div>
    );
  }
}

new Promise(function(resolve, reject) {
    WebAPI.login("Oinkers", "ROOFTOWN")
        .then(function (response) {
            console.log(response)
            resolve(response)
        }, function(err) {
            reject(err)
            console.log(err)
        })
})
    .then(function(response) {
        WebAPI.getRatSightings(1000000000000,100)
            .then(function (response) {
                console.log(response)
            }, function(err) {
                console.log(err)
            })
    })




export default App;
