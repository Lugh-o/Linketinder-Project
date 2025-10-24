import "./styles/main.css";

import { registrationScreen } from "./pages/registrationScreen/registrationScreen";
import { IdGenerator } from "./persistency/IdGenerator";
import { Store } from "./persistency/Store";
import { LocalStorage } from "./persistency/LocalStorage";
import { Router } from "./utils/Router";
import { AppContext } from "./utils/AppContext";

const appContainer: HTMLBodyElement | null = document.querySelector("body");
const idGenerator: IdGenerator = new IdGenerator();
const localStorage: LocalStorage = new LocalStorage();
const router: Router = new Router();
const store: Store = new Store(localStorage, idGenerator);
const appContext: AppContext = new AppContext(store, router);

if (appContainer) {
	appContainer.innerHTML = "";
	appContainer.appendChild(registrationScreen(appContext));
}
