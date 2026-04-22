<script setup lang="ts">
import { computed, nextTick, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Link, ArrowDown } from '@element-plus/icons-vue'
import * as aiApi from '../api/ai'

type ChatMsg = {
  id: string
  role: 'user' | 'assistant'
  content: string
  usedModel?: boolean
  citations?: aiApi.Citation[]
  clarifyingQuestions?: string[]
}

const props = defineProps<{
  modelValue: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean): void
}>()

const open = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})

const sending = ref(false)
const input = ref('')
const messages = ref<ChatMsg[]>([
  {
    id: crypto.randomUUID(),
    role: 'assistant',
    content:
      '我是药智助手。我目前只回答“内部流程 / 系统使用”问题，并基于本地 kb/ Markdown 知识库检索后给出答案。',
  },
])

const scrollRef = ref<HTMLElement | null>(null)
const autoScroll = ref(true)

function scrollToBottom() {
  nextTick(() => {
    const el = scrollRef.value
    if (!el) return
    if (autoScroll.value) el.scrollTop = el.scrollHeight
  })
}

async function send() {
  const text = input.value.trim()
  if (!text) return
  input.value = ''
  sending.value = true
  const userMsg: ChatMsg = { id: crypto.randomUUID(), role: 'user', content: text }
  messages.value.push(userMsg)
  scrollToBottom()

  try {
    const res = await aiApi.chat({ message: text, topK: 6 })
    messages.value.push({
      id: crypto.randomUUID(),
      role: 'assistant',
      content: res.reply,
      usedModel: res.usedModel,
      citations: res.citations ?? [],
      clarifyingQuestions: res.clarifyingQuestions ?? [],
    })
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '药智助手请求失败')
  } finally {
    sending.value = false
    scrollToBottom()
  }
}

function quickAsk(q: string) {
  if (sending.value) return
  input.value = q
  send()
}

function onScroll() {
  const el = scrollRef.value
  if (!el) return
  autoScroll.value = el.scrollTop + el.clientHeight >= el.scrollHeight - 40
}
</script>

<template>
  <el-drawer v-model="open" size="440px" :with-header="false" class="assistant-drawer">
    <div class="assistant-root">
      <div class="assistant-header">
        <div class="title">
          <el-icon><ChatDotRound /></el-icon>
          <span>药智助手</span>
        </div>
        <div class="hint">支持：内部流程 / 系统使用（知识库检索 + 可选大模型）</div>
      </div>

      <div ref="scrollRef" class="assistant-body" @scroll.passive="onScroll">
        <div v-for="m in messages" :key="m.id" class="msg" :class="m.role">
          <div class="bubble">
            <div class="content">{{ m.content }}</div>
            <div v-if="m.role === 'assistant' && m.citations && m.citations.length" class="citations">
              <div class="cit-title">
                引用来源
                <span class="cit-badge">{{ m.usedModel ? '模型生成' : '知识库兜底' }}</span>
              </div>
              <div v-for="c in m.citations" :key="c.path" class="cit-item">
                <el-icon class="cit-ico"><Link /></el-icon>
                <span class="cit-text">{{ c.title }}（{{ c.path }}）</span>
              </div>
            </div>

            <div
              v-if="m.role === 'assistant' && m.clarifyingQuestions && m.clarifyingQuestions.length"
              class="clarify"
            >
              <div class="clarify-title">我需要你确认</div>
              <div class="clarify-list">
                <el-button
                  v-for="q in m.clarifyingQuestions"
                  :key="q"
                  size="small"
                  plain
                  class="clarify-btn"
                  @click="quickAsk(q)"
                >
                  {{ q }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <div v-if="!autoScroll" class="scroll-tip" @click="autoScroll = true; scrollToBottom()">
          <el-icon><ArrowDown /></el-icon>
          回到底部
        </div>
      </div>

      <div class="assistant-footer">
        <el-input
          v-model="input"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4 }"
          placeholder="例如：员工为什么看不到日志审计？采购入库怎么操作？401/403 怎么处理？"
          :disabled="sending"
          @keydown.enter.exact.prevent="send"
        />
        <div class="actions">
          <el-button type="primary" :loading="sending" @click="send">发送</el-button>
        </div>
      </div>
    </div>
  </el-drawer>
</template>

<style scoped>
.assistant-drawer :deep(.el-drawer__body) {
  padding: 0;
}

.assistant-root {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.assistant-header {
  padding: 16px 16px 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  background: linear-gradient(180deg, rgba(24, 144, 255, 0.08), rgba(24, 144, 255, 0));
}

.assistant-header .title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 700;
  color: #303133;
}

.assistant-header .hint {
  margin-top: 6px;
  font-size: 12px;
  color: #909399;
}

.assistant-body {
  flex: 1;
  overflow: auto;
  overflow-x: hidden;
  padding: 16px;
  background: #f5f7fb;
}

.msg {
  display: flex;
  margin-bottom: 14px;
}

.msg.user {
  justify-content: flex-end;
}

.bubble {
  max-width: 92%;
  border-radius: 14px;
  padding: 12px 12px;
  line-height: 1.7;
  white-space: pre-wrap;
  overflow-wrap: anywhere;
  word-break: break-word;
  box-shadow: 0 8px 18px rgba(0, 0, 0, 0.06);
}

.msg.user .bubble {
  background: linear-gradient(135deg, #1890ff, #1677ff);
  color: #fff;
  border-bottom-right-radius: 6px;
}

.msg.assistant .bubble {
  background: #fff;
  color: #303133;
  border-bottom-left-radius: 6px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.content {
  font-size: 13px;
}

.citations {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px dashed rgba(0, 0, 0, 0.08);
}

.cit-title {
  font-size: 12px;
  color: #606266;
  font-weight: 600;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cit-badge {
  font-size: 12px;
  color: #1677ff;
  background: rgba(22, 119, 255, 0.08);
  padding: 2px 8px;
  border-radius: 999px;
  font-weight: 600;
}

.cit-item {
  display: flex;
  gap: 6px;
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}

.cit-ico {
  margin-top: 2px;
  color: #909399;
}

.cit-text {
  flex: 1;
}

.assistant-footer {
  padding: 12px 14px 14px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  background: #fff;
}

.clarify {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px dashed rgba(0, 0, 0, 0.08);
}

.clarify-title {
  font-size: 12px;
  color: #606266;
  font-weight: 600;
  margin-bottom: 8px;
}

.clarify-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.clarify-list :deep(.el-button) {
  border-radius: 999px;
}

.clarify-btn {
  width: 100%;
  justify-content: flex-start;
  text-align: left;
  white-space: normal;
  line-height: 1.35;
  padding: 8px 12px;
}

.clarify-btn :deep(span) {
  display: block;
  white-space: normal;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

.scroll-tip {
  position: sticky;
  bottom: 10px;
  margin: 0 auto;
  width: fit-content;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(0, 0, 0, 0.55);
  color: #fff;
  font-size: 12px;
  cursor: pointer;
  user-select: none;
}
</style>

