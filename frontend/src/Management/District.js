import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';

function District() {

    const [districts, setDistricts] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [units, setUnits] = useState([]);
    const [regions, setRegions] = useState([]);
    const [nameDistrict, setNameDistrict] = useState();
    const [idDistrict, setIdDistrict] = useState();
    const [regionDistrict, setRegionDistrict] = useState();
    const [unitDistrict, setUnitDistrict] = useState();


    const fetchFulldistrict = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/');
            setDistricts(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchFullRegions = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/administrativeRegion/');
            setRegions(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetaildistrict = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/' + code);
            setIddistrict(response.data.code)
            setNamedistrict(response.data.name)
            setRegiondistrict(response.data.administrativeRegion.id)
            setUnitdistrict(response.data.administrativeUnit.id)
            setShowEdit(true)
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFulldistrict();
        fetchFullRegions();
    }, [])

    const listRegionItems = regions.map((region) =>
        <option key={region.id} value={region.id}>{region.id + ". " + region.name}</option>
    )

    const formatData = (districts) => {
        return districts.map((item) => ({ ...item, isActive: false }))
    }

    const Modaldistrict = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo tỉnh/thành phố</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên tỉnh/thành phố (*)</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho tỉnh/thành phố (**)</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select><option value={1}>1. Thành phố trực thuộc trung ương</option><option value={2}>2. Tỉnh</option></Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Khu vực</Form.Label>
                            <Form.Select>{listRegionItems}</Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên tỉnh/thành phố không được trùng lặp với tên tỉnh/thành phố đã được khai báo</p>
                        <p>(**) Mã số của tỉnh/thành phố không được trùng lặp với mã số của tỉnh/thành phố đã được khai báo</p>
                    </div>
                    <Button variant="secondary" onClick={() => { setShow(false) }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => { setShow(false) }}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const ModalEditdistrict = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật tỉnh/thành phố</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên tỉnh/thành phố</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                                defaultValue={nameDistrict}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã tỉnh/thành phố</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                                defaultValue={iddistrict}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select defaultValue={unitdistrict}><option value={1}>1. Thành phố trực thuộc trung ương</option><option value={2}>2. Tỉnh</option></Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Khu vực</Form.Label>
                            <Form.Select defaultValue={regiondistrict}>{listRegionItems}</Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        districts.map((item) => {
                            if (item.code === iddistrict) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        setShowEdit(false)
                        districts.map((item) => {
                            if (item.code === iddistrict) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const listdistricts = districts.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetaildistrict(post.code)
            post.isActive = true
        }} className="rowTable" style={{ backgroundColor: (post.isActive) ? "yellow" : "white" }}>
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
                            {listdistricts}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className="buttonAdd" onClick={() => { setShow(true) }}>+ Khai báo tỉnh/thành phố</Button>
            <div>
                <TableResidential />
                {(show) ? <Modaldistrict /> : null}
                <ModalEditdistrict />
            </div>
        </div>
    );
}

export default District;
