import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';
import templates from './View.soy';
import { Config } from 'metal-state';

class View extends Component {
	showCards() {
		this.displayStyle = 'cards';
	}

	showList() {
		this.displayStyle = 'list';
	}
}

View.STATE = {
	displayStyle: Config.oneOf(['cards', 'list']).value('cards')
}

// Register component
Soy.register(View, templates);

export default View;