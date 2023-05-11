import Login from './Login/login';
import Residential from './Residential/residential';
import FindResidential from './Residential/FindResidential'
import Province from './Management/Province';
import DataEntry from './Residential/DataEntry';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './Home';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/login' element={<Login />} />
        <Route path='/residential' element={<Residential />} />
        <Route path='/findresidential' element={<FindResidential />} />
        <Route path='/province' element={<Province />} />
        <Route path='/citizeninput' element={<DataEntry />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
