import React from 'react';
import LoadingSpinner from '../components/loading-spinner/LoadingSpinner';

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error) {
    console.error(error);
    return { hasError: true };
  }

  render() {
    if (this.state.hasError) {
      return <LoadingSpinner pageSpinner={true}/>;
    }

    return this.props.children;
  }
}
