import './styles/main.css';
import { initApp } from './app';

const appContainer = document.getElementById('app');
if (appContainer) {
  appContainer.appendChild(initApp());
}