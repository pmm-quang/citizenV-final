import Login from './Login/login';
import FindResidential from './Residential/FindResidential'
import Province from './Management/Province';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Account from './Management/Account';
import Ward from './Management/Ward';
import Citizen from './Residential/Citizen' 
import CitizenInput from './Residential/CitizenInput' 
import District from './Management/District';
import Statis from './Statis/BasicStatis'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login />} />
        <Route path='/login' element={<Login />} />
        <Route path='/citizen' element={<Citizen />} />
        <Route path='/findresidential' element={<FindResidential />} />
        <Route path='/province' element={<Province />} />
        <Route path='/district' element={<District />} />
        <Route path='/citizeninput' element={<CitizenInput />} />
        <Route path='/account' element={<Account />} />
        <Route path='/statis' element = {<Statis />} />
        <Route path='/home' element = {<Home />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
