import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';
import templates from './Edit.soy';

class Edit extends Component {}

// Register component
Soy.register(Edit, templates);

export default Edit;