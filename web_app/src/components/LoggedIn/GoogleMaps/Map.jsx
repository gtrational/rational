import React, { Component } from 'react';
import ReactDOM from 'react-dom';

// NOTE: Implementation of Google Maps with React App followed from : https://www.fullstackreact.com/articles/how-to-write-a-google-maps-react-component/#

export class Map extends Component {
  componentDidUpdate(prevProps, prevState) {
    if (prevProps.google != this.props.google) {
      this.loadMap();
    }
  }

  loadMap() {
    if (this.props && this.props.google) {
      const {google} = this.props;
      const maps = google.maps

      const mapRef = this.refs.map
      const node = ReactDOM.findDOMNode(mapRef)

      let zoom = 10
      let lat = 37.774929;
      let long = 131.044;

      const center = new maps.LatLng(lat, long);
      const mapConfig = Object.assign({}, {
        center: center,
        zoom: zoom
      })

      var myLatLng = {lat: lat, lng: long};

      this.map = new maps.Map(node, mapConfig)

      const marker = new google.maps.Marker({
          position: myLatLng,
          map: node,
          title: 'Hello World!'
        });

      this.renderMarkers(this.map)
    }
  }

  render() {
    return (
      // copied from https://github.com/fullstackreact/google-maps-react/blob/master/examples/components/withMarkers.js#L10
      <Map google={this.props.google}
          style={{width: '100%', height: '100%', position: 'relative'}}
          className={'map'}
          zoom={14}>
      </Map>
    )
  }
}
