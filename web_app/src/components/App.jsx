import React, { Component } from 'react'
import { BrowserRouter as Router, Route, IndexRoute } from 'react-router-dom'

import NotFound from '../components/global/NotFound'
import LoggedOut from '../components/LoggedOut'
import LoggedIn from '../components/LoggedIn'

function requireLoggedIn(nextState, replaceState) {
    // TODO: check logged in
}

export default class App extends Component {
    render() {            
        return (
        <Router>
            <div>
                <Route path='/' component={LoggedOut}>
                    <h1>Test</h1>
                </Route>
                <Route path='/user' component={LoggedIn} onEnter={requireLoggedIn} />
                <Route path='*' component={NotFound} />
            </div>
        </Router>
        );
    }
}