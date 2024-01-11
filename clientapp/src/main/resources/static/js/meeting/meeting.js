const stomp = new StompJs.Client({
    brokerURL: 'ws://localhost:3000/ws'
});
const peer = new peerjs.Peer()

stomp.onStompError = (frame) => {
    console.log(`ERROR: ${frame}`)
}

$(async function () {
    const stream = await navigator.mediaDevices.getUserMedia({audio: true, video: true})
    const peers = {}

    peer.on('open', (currentId) => {
        console.log(`PEER OPENED ${currentId}`)

        // Create video for current profile
        const videoItem = createVideoItem(currentId, stream, true, profile)
        $('.video-container').append(videoItem)

        peer.on('call', (call) => {
            call.answer(stream, {metadata: profile})

            call.on('stream', (stream) => {
                if (peers[call.peer]) return
                else peers[call.peer] = true

                const otherProfile = call.metadata.profile

                const videoItem = createVideoItem(call.peer, stream, false, otherProfile)
                $('.video-container').append(videoItem)
            })
        })

        stomp.onConnect = (frame) => {
            console.log(`STOMP CONNECTED ${frame}`)

            stomp.publish({
                destination: `/app/meeting/${MEETING_ID}/connect`,
                body: JSON.stringify({
                    id: currentId,
                    name: profile.name
                })
            })

            stomp.subscribe(`/topic/meeting/${MEETING_ID}/connect`, (frame) => {
                const {id, name} = JSON.parse(frame.body)
                if (id === currentId) return

                console.log(`MEMBER CONNECTED ${id} ${name}`)

                const call = peer.call(id, stream, {metadata: {profile}});

                call.on('stream', (stream) => {
                    if (peers[call.peer]) return
                    else peers[call.peer] = true

                    const otherProfile = call.metadata.profile

                    const videoItem = createVideoItem(call.peer, stream, false, otherProfile)
                    $('.video-container').append(videoItem)
                })
            })

            stomp.subscribe(`/topic/meeting/${MEETING_ID}/disconnect`, (frame) => {
                const {id, name} = JSON.parse(frame.body)
                console.log(`MEMBER DISCONNECTED ${id} ${name}`)

                $(`*[data-peer-id="${id}"]`).remove()

                delete peers[id]
            })
        }

        stomp.activate()
    })
})

function createVideoItem(id, stream, muted, owner) {
    const videoItem = $(`
        <div class="video-item" data-peer-id="${id}">
            <img 
              class="avatar avatar-xl" 
              src="${owner.photo || `https://api.dicebear.com/7.x/adventurer-neutral/svg?seed=${owner.name}`}" 
              alt="${owner.name}"
            >
             <video></video>
            <div class="video-info">
                <span class="name">${owner.name}</span>
                <div>
                    <button>
                      <i class='bx bx-microphone bx-sm'></i>
                    </button>
                </div>
            </div>
        </div>
    `
    )

    const video = $(`<video id="video-${id}"></video>`)
    video.prop('muted', muted)
    video.prop('srcObject', stream)
    video.on('loadedmetadata', () => {
        video[0].play()
    })

    videoItem.find('video').replaceWith(video)

    console.log(videoItem)

    return videoItem;
}
