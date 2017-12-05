import React, { Component } from 'react'
import { Route, Switch } from 'react-router-dom'

import Home from './Home'
import Login from './Login'
import Register from './Register'

export default class LoggedOut extends Component {
    render() {
        // Render nav bar
        return (
            <div>
                <p>You are logged out you trash</p>
                <Switch>
                    <Route path='/' component={Home} />
                    <Route path='/login' component={Login} />
                    <Route path='/register' component={Register} />
                </Switch>
            </div>
        )
    }
}
