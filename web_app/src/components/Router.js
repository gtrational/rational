import React, { Component } from 'react'
import { IndexRoute, Route, Router } from 'react-router'

import history from 'services/history'
import NotFound from 'components/global/NotFound'


function requireLoggedIn(nextState, replaceState) {
    // TODO: check logged in
}


export default class Router extends Component {
    render = () => {
        <Router history={history}>
            <Route path='/' component={LoggedOut}>
                // TODO: Add some dank components
            </Route>

            <Route path='/user' component={LoggedIn} onEnter={requireLoggedIn}>

            </Route>
            <Route path='*' component={NotFound} />
        </Router>
    }
}