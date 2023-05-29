import Login from '../Login/login';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from '../Home';
import Statis from '../Statis/BasicStatis'
import FindResidential from '../Management/Citizen/Controller/FindResidential'
import Account from '../Management/Account/Controller/Account';
import Citizen from '../Management/Citizen/Controller/Citizen' 
import CitizenInput from '../Management/Citizen/Controller/CitizenInput' 
import Province from '../Management/AdministrativeUnits/Controler/Province';
import District from '../Management/AdministrativeUnits/Controler/District';
import Ward from '../Management/AdministrativeUnits/Controler/Ward';
import Hamlet from '../Management/AdministrativeUnits/Controler/Hamlet';
import InfoAccount from '../Management/Account/Controller/InfoAccount';

function Router() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login />} />
        <Route path='/login' element={<Login />} />
        <Route path='/citizen' element={<Citizen />} />
        <Route path='/findresidential' element={<FindResidential />} />
        <Route path='/province' element={<Province />}/> 
        <Route path='/district' element={<District />} />
        <Route path='/ward' element={<Ward />} /> 
        <Route path='/hamlet' element={<Hamlet />} />
        <Route path='/citizeninput' element={<CitizenInput />} /> 
        <Route path='/account' element={<Account />} /> 
        <Route path='/statis' element = {<Statis />} />
        <Route path='/home' element = {<Home />} />
        <Route path='infoAccount' element = {<InfoAccount />} />
      </Routes>
    </BrowserRouter>
  );
}

export default Router;
