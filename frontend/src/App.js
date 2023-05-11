import Login from './Login/login';
import Residential from './Residential/residential';
import FindResidential from './Residential/FindResidential'
import Province from './Management/Province';
import DataEntry from './Residential/DataEntry';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Account from './Management/Account';
<<<<<<< Updated upstream
import Ward from './Management/Ward';
=======
>>>>>>> Stashed changes
import District from './Management/District';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/login' element={<Login />} />
        <Route path='/residential' element={<Residential />} />
        <Route path='/findresidential' element={<FindResidential />} />
        <Route path='/province' element={<Province />} />
        <Route path='/district' element={<District />} />
        <Route path='/citizeninput' element={<DataEntry />} />
        <Route path='/account' element={<Account />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
