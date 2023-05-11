import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';

function Ward() {

    const [wards, setWards] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [units, setUnits] = useState([]);
    const [nameWard, setNameWard] = useState();
    const [idWard, setIdWard] = useState();
    const [unitWard, setUnitWard] = useState();


    const fetchFullWard = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/');
            setWards(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailWard = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/' + code);
            setIdWard(response.data.code)
            setNameWard(response.data.name)
            setUnitWard(response.data.administrativeUnit.id)
            setShowEdit(true)
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFullWard();
    }, [])

    const formatData = (wards) => {
        return wards.map((item) => ({ ...item, isActive: false }))
    }

    const ModalWard = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo Xã/Phường/Thị trấn</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Xã/Phường/Thị trấn (*)</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho Xã/Phường/Thị trấn (**)</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select><option value={1}>1. Xã</option><option value={2}>2. Phường</option><option value={3}>3.Thị trấn</option></Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>

                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên Xã/Phường/Thị trấn không được trùng lặp với tên Xã/Phường/Thị trấn đã được khai báo</p>
                        <p>(**) Mã số của Xã/Phường/Thị trấn không được trùng lặp với mã số của Xã/Phường/Thị trấn đã được khai báo</p>
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

    const ModalEditWard = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật Xã/Phường/Thị trấn</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Xã/Phường/Thị trấn</Form.Label>
                            <Form.Control
                                type="text"
                                autoFocus
                                defaultValue={nameWard}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã Xã/Phường/Thị trấn</Form.Label>
                            <Form.Control
                                type="number"
                                autoFocus
                                defaultValue={idWard}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select><option value={1}>1. Xã</option><option value={2}>2. Phường</option><option value={3}>3.Thị trấn</option></Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        wards.map((item) => {
                            if (item.code === idWard) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        setShowEdit(false)
                        wards.map((item) => {
                            if (item.code === idWard) {
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

    const listWards = wards.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetailWard(post.code)
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
                                <th>Xã/Phường/Thị trấn</th>
                                <th>Khu vực</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listWards}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className="buttonAdd" onClick={() => { setShow(true) }}>+ Xã/Phường/Thị trấn</Button>
            <div>
                <TableResidential />
                {(show) ? <ModalWard /> : null}
                <ModalEditWard />
            </div>
        </div>
    );
}

export default Ward;
