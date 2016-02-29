#![crate_type = "dylib"]

#[macro_use]
extern crate lazy_static;

use std::sync::Mutex;
use std::collections::HashSet;

#[repr(C)]
pub struct Vec3d {
    x: f64,
    y: f64,
    z: f64,
}

#[derive(Eq, PartialEq, Hash)]
struct Chunk {
    x: i32,
    y: i32,
    z: i32,
    blocks: [[[i32; 16]; 16]; 16],
}

lazy_static! {
    static ref CHUNKS: Mutex<HashSet<Chunk>> = Mutex::new(HashSet::new());
}

#[no_mangle]
pub extern fn add_chunk(cx: i32, cy: i32, cz: i32, c_blocks: [[[i32; 16]; 16]; 16]) {
    CHUNKS.lock().unwrap().insert(Chunk {x: cx, y: cy, z: cz, blocks: c_blocks});
}

#[no_mangle]
pub extern fn update_block(x: i32, y: i32, z: i32, id: i32) {
    let cx: i32 = x / 16;
    let cy: i32 = y / 16;
    let cz: i32 = z / 16;
    
    let rx: i32 = if x > 0 { x % 16 } else { 16 + (x % 16) };
    let ry: i32 = if y > 0 { y % 16 } else { 16 + (y % 16) };
    let rz: i32 = if z > 0 { z % 16 } else { 16 + (z % 16) };
    
    for c in CHUNKS.lock().unwrap().iter() {
        if c.x == cx && c.y == cy && c.z == cz {
            c.blocks[rx as usize][ry as usize][rz as usize] = id;
        }
    }
}