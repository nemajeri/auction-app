import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  constructor(connectionString) {
    this.socket = new SockJS(process.env.REACT_APP_BASE_URL.replace('/api/v1', connectionString));
    this.stompClient = new Client({
      webSocketFactory: () => this.socket,
    });
  }

  connect(onConnect) {
    this.stompClient.onConnect = () => {
      setTimeout(() => {
        onConnect();
    }, 1000);  
    };

    this.stompClient.onStompError = (frame) =>  {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };

    this.stompClient.onDisconnect = (frame) => {
      console.log('Disconnected: ' + frame);
    };

    this.stompClient.onWebSocketClose = (event) => {
      console.log('Websocket closed. Event: ' + event);
    };

    this.stompClient.onWebSocketError = (event) => {
      console.log('Websocket error. Event: ' + event);
    };

    this.stompClient.activate();
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  subscribe(destination, callback, headers) {
    return this.stompClient.subscribe(destination, callback, headers);
  }

  send(destination, body, headers = {}) {
    this.stompClient.publish({
      destination,
      headers,
      body: JSON.stringify(body),
    });
  }
}

export default WebSocketService;
