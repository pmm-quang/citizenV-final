import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';

function Province() {

    const [provinces, setProvinces] = useState([]);


    const fetchFullProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/');
            setProvinces(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFullProvince();
    }, [])

    const listProvinces = provinces.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {}}>
            <td>{post.code}</td>
            <td>{post.name}</td>
            <td>{post.administrativeUnit.fullName}</td>
            <td>{post.administrativeRegion.name}</td>
        </tr>
    );

    const TableResidential = () => {
        return (
            <div>
                <div>
                    <Table striped bordered hover size="sm" className="tableResidential">
                        <thead>
                            <tr>
                                <th>Mã</th>
                                <th>Đơn vị</th>
                                <th>Tỉnh/Thành phố</th>
                                <th>Khu vực</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listProvinces}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className = "buttonAdd">+ Khai báo tỉnh/thành phố</Button>
            <div>
                <TableResidential />
            </div>
        </div>
    );
}

export default Province;
