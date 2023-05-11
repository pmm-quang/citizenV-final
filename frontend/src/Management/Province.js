import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';

function Province() {

    const [provinces, setProvinces] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [units, setUnits] = useState([]);
    const [regions, setRegions] = useState([]);
    const [nameProvince, setNameProvince] = useState();
    const [idProvince, setIdProvince] = useState();
    const [regionProvince, setRegionProvince] = useState();
    const [unitProvince, setUnitProvince] = useState();


    const fetchFullProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/');
            setProvinces(response.data);
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

    const fetchDetailProvince = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/' + code);
            setIdProvince(response.data.code)
            setNameProvince(response.data.name)
            setRegionProvince(response.data.administrativeRegion.id)
            setUnitProvince(response.data.administrativeUnit.id)
            setShowEdit(true)
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFullProvince();
        fetchFullRegions();
    }, [])

    const listRegionItems = regions.map((region) =>
        <option key={region.id} value={region.id}>{region.id + ". " + region.name}</option>
    )

    const formatData = (provinces) => {
        return provinces.map((item) => ({ ...item, isActive: false }))
    }

    const ModalProvince = () => {
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

    const ModalEditProvince = () => {
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
                                defaultValue={nameProvince}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã tỉnh/thành phố</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                                defaultValue={idProvince}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select defaultValue={unitProvince}><option value={1}>1. Thành phố trực thuộc trung ương</option><option value={2}>2. Tỉnh</option></Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Khu vực</Form.Label>
                            <Form.Select defaultValue={regionProvince}>{listRegionItems}</Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        provinces.map((item) => {
                            if (item.code === idProvince) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        setShowEdit(false)
                        provinces.map((item) => {
                            if (item.code === idProvince) {
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

    const listProvinces = provinces.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetailProvince(post.code)
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
            <Button className="buttonAdd" onClick={() => { setShow(true) }}>+ Khai báo tỉnh/thành phố</Button>
            <div>
                <TableResidential />
                {(show) ? <ModalProvince /> : null}
                <ModalEditProvince />
            </div>
        </div>
    );
}

export default Province;
