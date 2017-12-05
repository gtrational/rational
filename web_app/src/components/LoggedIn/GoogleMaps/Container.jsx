import React, { Component } from 'react';
//import Map from './Map'
import {Map, InfoWindow, Marker, GoogleApiWrapper} from 'google-maps-react';
import ReactDOM from 'react-dom';

export class Container extends Component {
  render() {
    if (!this.props.loaded) {
      return (
        <div>"Loading..."</div>
      )
    } // props stands for "properties" and are sort of like the arguments of a main method.
    const style = {
      width: '100vw',
      height: '100vh'
    }
    
    return (
      <div>
      <Map google={this.props.google} />
      </div>
    )
  }
}

export default GoogleApiWrapper({
  apiKey: ('AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo')
})(Container)
