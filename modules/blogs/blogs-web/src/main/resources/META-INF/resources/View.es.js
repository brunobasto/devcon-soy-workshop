import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';
import templates from './View.soy';

class View extends Component {}

// Register component
Soy.register(View, templates);

export default View;