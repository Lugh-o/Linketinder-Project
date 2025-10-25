export class LocalStorage {
	save(data: object): void {
		localStorage.setItem("store", JSON.stringify(data));
	}

	load(): any | null {
		const saved = localStorage.getItem("store");
		return saved ? JSON.parse(saved) : null;
	}
}
