import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  constructor() {
    this.socket = new SockJS('http://localhost:8080/ws/notifications');
    this.socket = new SockJS('http://localhost:8080/ws/notifications');
    this.stompClient = new Client({
      webSocketFactory: () => this.socket,
    });
  }

  connect(headers, onConnect) {
    this.stompClient.onConnect = (frame) => {
      onConnect();
    };

    this.stompClient.onStompError = function (frame) {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };

    this.stompClient.onDisconnect = function (frame) {
      console.log('Disconnected: ' + frame);
    };

    this.stompClient.onWebSocketClose = function (event) {
      console.log('Websocket closed. Event: ' + event);
    };

    this.stompClient.onWebSocketError = function (event) {
      console.log('Websocket error. Event: ' + event);
    };

    this.stompClient.activate();
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  subscribe(destination, callback) {
    return this.stompClient.subscribe(destination, callback);
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
