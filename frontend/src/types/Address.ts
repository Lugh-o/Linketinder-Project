export class Address {
	state: string;
	postalCode: string;
	country: string;
	city: string;
	street: string;

	constructor(
		state: string,
		postalCode: string,
		country: string,
		city: string,
		street: string
	) {
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.city = city;
		this.street = street;
	}
}
