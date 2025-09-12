export function initApp(): HTMLElement {
  const container = document.createElement('div');
  container.className = 'app-container';

  const title = document.createElement('h1');
  title.textContent = 'Hello TypeScript + Vite!';

  const button = document.createElement('button');
  button.textContent = 'Click me';
  button.addEventListener('click', () => {
    alert('You clicked the button!');
  });

  container.appendChild(title);
  container.appendChild(button);

  return container;
}
