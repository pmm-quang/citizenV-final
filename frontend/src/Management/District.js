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
    const [nameDistrict, setNameDistrict] = useState();
    const [idDistrict, setIdDistrict] = useState();
    const [unitDistrict, setUnitDistrict] = useState();


    const fetchFullDistrict = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/');
            setDistricts(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailDistrict = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/' + code);
            setIdDistrict(response.data.code)
            setNameDistrict(response.data.name)
            setUnitDistrict(response.data.administrativeUnit.id)
            setShowEdit(true)
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFullDistrict();
    }, [])

    const formatData = (districts) => {
        return districts.map((item) => ({ ...item, isActive: false }))
    }

    const ModalDistrict = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo Quận/Huyện/Thị xã</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Quận/Huyện/Thị xã (*)</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho Quận/Huyện/Thị xã (**)</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select><option value={1}>1. Thành phố trực thuộc tỉnh</option><option value={2}>2. Quận</option><option value={3}>3.Huyện</option><option value={4}>4. Thị xã</option></Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>

                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên Quận/Huyện/Thị xã không được trùng lặp với tên Quận/Huyện/Thị xã đã được khai báo</p>
                        <p>(**) Mã số của Quận/Huyện/Thị xã không được trùng lặp với mã số của Quận/Huyện/Thị xã đã được khai báo</p>
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

    const ModalEditDistrict = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật Quận/Huyện/Thị xã</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Quận/Huyện/Thị xã</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                                defaultValue={nameDistrict}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã Quận/Huyện/Thị xã</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                                defaultValue={idDistrict}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select defaultValue={unitDistrict}><option value={1}>1. Thành phố trực thuộc tỉnh</option><option value={2}>2. Quận</option><option value={3}>3.Huyện</option><option value={4}>4. Thị xã</option></Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        districts.map((item) => {
                            if (item.code === idDistrict) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        setShowEdit(false)
                        districts.map((item) => {
                            if (item.code === idDistrict) {
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

    const listDistricts = districts.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetailDistrict(post.code)
            post.isActive = true
        }} className="rowTable" style={{ backgroundColor: (post.isActive) ? "yellow" : "white" }}>
            <td>{post.code}</td>
            <td>{post.name}</td>
            <td>{post.administrativeUnit.fullName}</td>
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
                                <th>Quận/Huyện/Thị Xã</th>
                                <th>Khu vực</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listDistricts}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className="buttonAdd" onClick={() => { setShow(true) }}>+ Quận/Huyện/Thị Xã</Button>
            <div>
                <TableResidential />
                {(show) ? <ModalDistrict /> : null}
                <ModalEditDistrict />
            </div>
        </div>
    );
}

export default District;