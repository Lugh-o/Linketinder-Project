import type { Store } from "../persistency/Store";
import type { Router } from "./Router";

export class AppContext {
	store: Store;
	router: Router;

	constructor(store: Store, router: Router) {
		this.store = store;
		this.router = router;
	}
}
